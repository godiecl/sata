#  Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.

import websocket

ws = websocket.WebSocket()
ws.connect("ws://localhost:8081/ws")
ws.send("Hello, World!")

print(ws.recv())
ws.close()
