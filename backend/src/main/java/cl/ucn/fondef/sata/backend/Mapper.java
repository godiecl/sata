/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import cl.ucn.fondef.sata.backend.repositories.views.ComponenteJpa;
import cl.ucn.fondef.sata.backend.repositories.views.MedidaJpa;
import cl.ucn.fondef.sata.backend.model.simulation.views.ComponenteView;
import cl.ucn.fondef.sata.backend.model.simulation.views.MedidaView;
import lombok.NonNull;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The Mapper Class.
 *
 * @author Diego Urrutia-Astorga.
 */
@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface Mapper {

    /**
     * The Mapper.
     */
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    /**
     * MedidaJpa to MedidaView.
     *
     * @param listMedidaJpa to map.
     * @return the list of MedidaView.
     */
    static MedidaView toListMedidaView(@NonNull List<MedidaJpa> listMedidaJpa,
                                       @NonNull Long idEjecucion,
                                       @NonNull Long idSimulacion,
                                       @NonNull ComponenteJpa componenteJpa) {

        return MedidaView.builder()
                         .idSimulacion(idSimulacion)
                         .idEjecucion(idEjecucion)
                         .componente(INSTANCE.toComponenteView(componenteJpa))
                         .datos(INSTANCE.toListDatoView(listMedidaJpa))
                         .build();
    }

    /**
     * ComponenteJpa to ComponenteView.
     *
     * @param componenteJpa to map.
     * @return the ComponenteView.
     */
    @Mapping(target = "id")
    @Mapping(target = "nombre")
    @Mapping(target = "descripcion")
    @Mapping(target = "unidad")
    @Mapping(target = "formula")
    @Mapping(target = "minimo")
    @Mapping(target = "warning")
    ComponenteView toComponenteView(ComponenteJpa componenteJpa);

    @Mapping(target = "senial")
    @Mapping(target = "valor")
    @Mapping(target = "fecha")
    MedidaView.DatoView toDatoView(MedidaJpa medidaJpa);

    List<MedidaView.DatoView> toListDatoView(List<MedidaJpa> listMedidaJpa);
}
