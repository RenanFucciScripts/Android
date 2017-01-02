<?php
require_once "init.php";

try{
    if (isset($_POST["cod_crianca"])) {
        $cod_crianca = $_POST["cod_crianca"];
        /**
         * Para trocar os campos:
         *      * Alterar e manter no mesmo padrão o @array_fiedls;
         *      * Deixar o url_out como ultima variavel no @array_fiedls no array
         * Para funcionar link no App Android:
         *      * Colocar true para a @alias e deixar o url_out como ultimo campo do SELECT;
         *      * @str_prefixo_url deve começar com http:
         *      * @str_alias NÃO pode ser alterado.
         **/
        $array_fiedls = array("`em_harpia_imagesin`.`file_in_name`",
            "`em_harpia_imagesin`.`date_in`"," `em_harpia_imagesin`.`date_out`",
            "`em_harpia_imagesin`.`last_stage`",
            "`em_harpia_imagesin`.`url_out`");//NAO MUDAR
        $str_prefixo_url = "http://harpia.profalexaraujo.com.br/files/";
        $alias = true;

        //... Nao alterar daqui para baixo. Se alterar só as vars de cima conforme o primeiro doc,
        //         será dinâmico no App Android
        $str_alias = "url_download";
        if($alias) {
            $str_select ="SELECT ".implode(', ', $array_fiedls)." AS ".$str_alias."
                        FROM `em_harpia_imagesin` WHERE `em_harpia_imagesin`.`id_child` = :c";
        }else{
            $str_select = "SELECT ".implode(', ', $array_fiedls)."
                        FROM `em_harpia_imagesin` WHERE `em_harpia_imagesin`.`id_child` = :c";
        }
        $pdo = new PDO(DB_DRIVER . ':host=' . DB_HOST . ';dbname=' . DB_DATABASE, DB_USER, DB_PWD);
        $slct = $pdo->prepare($str_select);
        $slct->bindParam(':c', $cod_crianca, PDO::PARAM_INT);
        $slct->execute();
        if ($slct->rowCount() > 0) {
            $rs_json = array();
            if($alias){
                $cont = 0;
                while ($row = $slct->fetch(PDO::FETCH_BOTH)) {
                    $array_aux = array();
                    for($i=0; $i<sizeof($array_fiedls); $i++){
                        if($i==sizeof($array_fiedls)-1){
                            $array_aux[$str_alias]= $str_prefixo_url.$row[$i];
                        }else{
                            $array_aux[str_replace("`","",explode(".", $array_fiedls[$i])[1])] = $row[$i];
                        }
                    }
                    array_push($rs_json, $array_aux);
                }
            }else {
                while ($row = $slct->fetch(PDO::FETCH_ASSOC)) {
                    array_push($rs_json, $row);
                }
            }
            print json_encode($rs_json);
        } else {
            echo "-1";
        }
    }else{
        echo "-2";
    }
} catch(Exception $e){
    echo "-3";
}

?>