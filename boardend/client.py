#  Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.

import json
import random
import time
from threading import Thread

import structlog
import websocket

websocket.enableTrace(True)
log = structlog.get_logger()

# the tarjeta used to retrieve the components in the board
TARJETA_ID = "RPI-001"

# the componentes
componentes = []
# the eventos
eventos = []


def retrieve_medidas_from_board():
    log.debug("Retrieving medidas ..")
    # TODO: Obtener las medidas desde la tarjeta arduino conectada a la RPI
    if (random.randint(0, 100) % 2) == 0:
        return json.dumps({
            "message": "Hello %d" % time.time(),
            "senial": random.randint(0, 100),
            "valor": random.randint(0, 100),
            "fecha": time.time(),  # <- check if this is the correct format
            "idComponente": 10,
            "idEjecucion": 7
        })
    else:
        return None


def send_medidas_to_backend(ws, medidas):
    if medidas is not None:
        log.debug("Sending medidas to server ..")
        ws.send(medidas)
    else:
        log.debug("No medidas to send.")


def retrieve_componentes_from_backend(ws, tarjeta_id):
    log.debug("Retrieving componentes from backend for %s board .." % tarjeta_id)
    # TODO: Obtener los componentes desde el backend
    return json.dumps([
        {
            "nombre": "Sensor de temperatura",
            "descripcion": "Thermistor RTD v2.0",
            "unidad": "Grados Celsius",
            "formula": "T \u003d 1 / (A + B * ln(R) + C * ln(R)^3)",
            "umbral_minimo": 0.0,
            "umbral_warning": 45.0,
            "umbral_error": 60.0,
            "tarjeta": "RPI-001",
            "estado_componente": "ESTADO_ACTIVO",
            "tipo_componente": "TIPO_SENSOR",
            "id_equipo": 2,
            "id": 4,
        },
        {
            "nombre": "Sensor de Humedad",
            "descripcion": "Sensor DHT11",
            "unidad": "Porcentaje",
            "formula": "H \u003d 0.51 * x",
            "umbral_minimo": 0.0,
            "umbral_warning": 90.0,
            "umbral_error": 100.0,
            "tarjeta": "RPI-001",
            "estado_componente": "ESTADO_ACTIVO",
            "tipo_componente": "TIPO_SENSOR",
            "id_equipo": 2,
            "id": 6,
        }
    ])


def retrieve_eventos_from_backend(ws, tarjeta_id):
    log.debug("Retrieving eventos from backend for %s board .." % tarjeta_id)
    # TODO: Obtener los eventos asociados a la tarjeta desde el backend


def on_message(ws, message):
    log.debug("Got a message: %s" % message)


def on_ping(ws, message):
    log.debug("Got a Ping!")


def on_pong(ws, message):
    log.debug("Got a Pong!")


def on_open(ws):
    global thread
    global stop_thread
    thread = Thread(target=run, args=(ws, lambda: stop_thread))
    thread.start()


def run(ws, stop):
    log.debug("Starting the thread ..")

    # retrieve componentes from the backend
    ws.send(json.dumps({
        "type": "RETRIEVE_COMPONENTES",
        "tarjeta": TARJETA_ID
    }))
    time.sleep(2)

    # retrieve the eventos from the backend
    ws.send(json.dumps({
        "type": "RETRIEVE_EVENTOS",
        "tarjeta": TARJETA_ID
    }))
    time.sleep(2)

    # retrieve the ejecucion from the backend
    ws.send(json.dumps({
        "type": "RETRIEVE_EJECUCION",
        "tarjeta": TARJETA_ID
    }))
    time.sleep(2)

    while stop() is False:
        # capture medidas from daughter board
        medidas = retrieve_medidas_from_board()
        # send to the server
        send_medidas_to_backend(ws, medidas)

        for i in range(20):
            if stop():
                log.debug("Stopping the thread ..")
                return
            time.sleep(0.25)


if __name__ == "__main__":

    wsapp = websocket.WebSocketApp("ws://satax.cl:8081/ws",
                                   on_message=on_message,
                                   on_ping=on_ping,
                                   on_pong=on_pong,
                                   on_open=on_open)

    thread = None
    stop_thread = False

    try:
        wsapp.run_forever(ping_interval=60, ping_timeout=30, reconnect=5)
    except KeyboardInterrupt:
        log.debug("Got keyboard interrupt!")
        stop_thread = True
        if thread is not None:
            thread.join()
        wsapp.close()
    finally:
        log.debug("Done.")
