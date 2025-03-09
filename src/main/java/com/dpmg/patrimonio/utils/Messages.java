package com.dpmg.patrimonio.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {
    public final String SUCCESS_FETCH = "Retornamos o(s) registro(s) que você pediu!";
    public final String EMPTY_LIST = "Não encontramos registros para a sua busca!";
    public final String FOUND_INVENTORY = "Inventário Patrimonial encontrado";
    public final String SUCCESS_UPDATE_STATUS = "Status do Inventário Patrimonial atualizado com sucesso!";
    public final String INVENTORY_NOT_FOUND = "Inventário Patrimonial não encontrado!";
    public final String CAN_NOT_UPDATE_ITEM_IF_INVENTORY_IS_NOT_OPEN = "Não é possível atualizar um item se o inventário não estiver aberto!";
    public final String INVALID_STATUS = "Status informado inválido";
    public final String ITEM_NOT_FOUND = "Item não encontrado";
    public final String ROUTE_NOT_FOUND = "Rota não encontrada";
    public final String METHOD_NOT_ALLOWED = "Método não permitido";
    public final String GENERIC_ERROR = "Ocorreu um erro inesperado, tente novamente mais tarde!";
    public final String GENERIC_VALIDATION_ERROR = "Ocorreu um erro de validação, verifique os campos informados!";
    public final String SUCCESS_CREATED_PATRIMONY = "Patrimônio cadastrado com sucesso!";
    public final String SUCCESS_UPDATED_PATRIMONY = "Patrimônio atualizado com sucesso!";
    public final String SUCCESS_DELETED_PATRIMONY = "Patrimônio excluído com sucesso!";
    public final String SUCCESS_SAVE_OBSERVATION = "Observação salva com sucesso!";
    public final String UNIT_NOT_FOUND = "Unidade não encontrada";
    public final String INVALID_OTHER_SITUATION = "A tentativa de alteração falhou. O ID fornecido não corresponde a uma 'outra situação'. ID: %s";
    public final String NOT_MANUAL_REGISTRATION = "A tentativa de alteração falhou. Esse item não corresponde a um registro manual.";
    public final String FAILED_TO_IMPORT_FILE = "Falha ao importar arquivo";
    public final String INVALID_IMPORT_FILE = "Arquivo inválido";
    public final String CAN_NOT_IMPORT_INVENTORY = "Não é possível importar um inventário após ele ter sido aberto/fechado/encerrado. Status atual: %s";
    public final String CAN_NOT_UPDATE_FINISHED_INVENTORY = "O inventário do ano %s já foi encerrado portanto não pode mais sofrer alterações";
    public final String IMPORT_ALREADY_IN_PROGRESS = "Já existe uma importação em andamento";
    public final String SHEET_INCORRET_NAME = "O nome da página da planilha está incorreto. O nome correto é: %s";
    public final String IMPORT_STARTED = "Importação iniciada, aguarde e receberá um e-mail ao final do processo";
}
