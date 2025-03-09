package com.dpmg.patrimonio.models.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PatrimonySituationEnum {
    // default situations
    INDEFINIDO("Indefinido"),
    LOCALIZADO("Localizado"),
    NAO_LOCALIZADO("Não Localizado"),

    // other situations
    CODIGO_DUPLICADO("Código Duplicado"),
    NAO_CONSTA_SISTEMA("Não Consta no Sistema"),
    NAO_CORRESPONDE_ITEM("Patrimônio não corresponde à descrição do item"),
    OUTRA_UNIDADE("Localizado em outra unidade"),
    SEM_PATRIMONIO("Sem Patrimônio");

    private final String label;

    PatrimonySituationEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Map<String, String> getLabels() {
        return Stream.of(INDEFINIDO, LOCALIZADO, NAO_LOCALIZADO)
                .collect(Collectors.toMap(Enum::name, PatrimonySituationEnum::getLabel));
    }

    public static Map<String, String> getOtherSituationLabels() {
        return Stream.of(CODIGO_DUPLICADO, NAO_CONSTA_SISTEMA, NAO_CORRESPONDE_ITEM, OUTRA_UNIDADE, SEM_PATRIMONIO)
                .collect(Collectors.toMap(Enum::name, PatrimonySituationEnum::getLabel));
    }
}
