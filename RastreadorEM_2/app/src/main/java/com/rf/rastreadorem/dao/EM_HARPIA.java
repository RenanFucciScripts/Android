/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rf.rastreadorem.dao;

/**
 * Interface para manter o padrão de nomes de todas as tabelas e campos do banco
 * de dados.
 *
 * @author RenanFucci
 */
public interface EM_HARPIA {

    public static final String DATABASE_NAME = "em_harpia";

    public String getAsString_ComCrase();

    public String getAsString_SemCrase();

    /**
     * Inner Enum para manter o padrão da tabela EM_CHILD.
     *
     * @author RenanFucci
     */
    public enum EM_CHILD implements EM_HARPIA {
        TABLE_NAME("`em_child`"),
        FIELD_ID_CHILD(TABLE_NAME.getAsString_ComCrase() + ".`em_child_id`"),
        FIELD_EM_NAME_CHILD(TABLE_NAME.getAsString_ComCrase() + ".`em_child_name`"),
        FIELD_EM_CHILD_ATIVO(TABLE_NAME.getAsString_ComCrase() + ".`em_child_ativo`"),
        FIELD_EM_CHILD_ANO(TABLE_NAME.getAsString_ComCrase() + ".`em_child_ano`");

        private final String asString;

        private EM_CHILD(String asString) {
            this.asString = asString;
        }

        @Override
        public String getAsString_ComCrase() {
            return asString;
        }

        @Override
        public String getAsString_SemCrase() {
            return asString.replaceAll("`", "");

        }
    }

    /**
     * Inner Enum para manter o padrão da tabela EM_HARPIA_IMAGESIN.
     *
     * @author RenanFucci
     */
    public enum EM_HARPIA_IMAGESIN implements EM_HARPIA {
        TABLE_NAME("`em_harpia_imagesin`"),
        FIELD_ID(TABLE_NAME.getAsString_ComCrase() + ".`id`"),
        FIELD_URL_IN(TABLE_NAME.getAsString_ComCrase() + ".`url_in`"),
        FIELD_URL_OUT(TABLE_NAME.getAsString_ComCrase() + ".`url_out`"),
        FIELD_DATE_IN(TABLE_NAME.getAsString_ComCrase() + ".`date_in`"),
        FIELD_DATE_OUT(TABLE_NAME.getAsString_ComCrase() + ".`date_out`"),
        FIELD_TYPE_FILE(TABLE_NAME.getAsString_ComCrase() + ".`type_file`"),
        FIELD_CONVERTED_FILE(TABLE_NAME.getAsString_ComCrase() + ".`converted_file`"),
        FIELD_ID_PARENT(TABLE_NAME.getAsString_ComCrase() + ".`id_parent`"),
        FIELD_STATUS_FILE(TABLE_NAME.getAsString_ComCrase() + ".`status_file`"),
        FIELD_THREAD(TABLE_NAME.getAsString_ComCrase() + ".`thread`"),
        FIELD_QR_CODE(TABLE_NAME.getAsString_ComCrase() + ".`qr_code`"),
        FIELD_ID_CHILD(TABLE_NAME.getAsString_ComCrase() + ".`id_child`"),
        FIELD_LAST_STAGE(TABLE_NAME.getAsString_ComCrase() + ".`last_stage`"),
        FIELD_FILE_OUT_NAME(TABLE_NAME.getAsString_ComCrase() + ".`file_out_name`"),
        FIELD_FILE_IN_NAME(TABLE_NAME.getAsString_ComCrase() + ".`file_in_name`"),
        FIELD_ID_CHILD_NEW(TABLE_NAME.getAsString_ComCrase() + ".`id_child_new`"),
        FIELD_URL_OUT_ILUSTRA(TABLE_NAME.getAsString_ComCrase() + ".`url_out_ilustra`"),
        FIELD_ID_SCHOOL(TABLE_NAME.getAsString_ComCrase() + ".`id_school`");
        private final String asString;

        private EM_HARPIA_IMAGESIN(String asString) {
            this.asString = asString;
        }

        @Override
        public String getAsString_ComCrase() {
            return asString;
        }

        @Override
        public String getAsString_SemCrase() {
            return asString.replaceAll("`", "");

        }
    }

    /**
     * Inner Enum para manter o padrão da tabela EM_SCHOOL.
     *
     * @author RenanFucci
     */
    public enum EM_SCHOOL implements EM_HARPIA {
        TABLE_NAME("`em_school`"),
        FIELD_ID_SCHOOL(TABLE_NAME.getAsString_ComCrase() + ".`em_school_id`"),
        FIELD_EM_NAME_SCHOOL(TABLE_NAME.getAsString_ComCrase() + ".`em_school_name`"),
        FIELD_EM_SCHOOL_ATIVO(TABLE_NAME.getAsString_ComCrase() + ".`em_school_ativo`");

        private final String asString;

        private EM_SCHOOL(String asString) {
            this.asString = asString;
        }

        @Override
        public String getAsString_ComCrase() {
            return asString;
        }

        @Override
        public String getAsString_SemCrase() {
            return asString.replaceAll("`", "");

        }
    }
}
