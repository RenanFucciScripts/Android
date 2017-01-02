<?php
require_once "init.php";

try{
    //echo ($_POST["cod_crianca"]);
    //if (isset($_POST["cod_crianca"])) {
    if (isset($_GET["cod_crianca"])) {

        //echo "if<br>";
        //$cod_crianca = $_POST["cod_crianca"];
        $cod_crianca = $_GET["cod_crianca"];
        //echo "$cod_crianca<br>";
        // echo "PDO: ".$pdo."<br>";
        /**
         * Para trocar os campos:
         *      * Alterar e manter no mesmo padrão o @array_fiedls;
         *      * Deixar o url_out como ultima variavel no @array_fiedls no array
         * Para funcionar link no android:
         *      colocar true para a @alias e deixar o url_out como ultimo campo do SELECT
         **/
        $array_fiedls = array("`em_harpia_imagesin`.`date_in`"," `em_harpia_imagesin`.`date_out`",
            "`em_harpia_imagesin`.`last_stage`","`em_harpia_imagesin`.`url_out`");
        //print_r(implode(', ', $array_fiedls));
        $alias = false;
        if($alias) {
            //echo "\ntrue";
            $str_select ="SELECT ".implode(', ', $array_fiedls)." AS url_download
                        FROM `em_harpia_imagesin` WHERE `em_harpia_imagesin`.`id_child` = :c";
            //echo "\nslct = ".$str_select;
        }else{
            //echo "\nfalse";
            $str_select = "SELECT ".implode(', ', $array_fiedls)."
                        FROM `em_harpia_imagesin` WHERE `em_harpia_imagesin`.`id_child` = :c";
            //echo "\nslct = ".$str_select;
        }
        //echo "\n\n";
        $pdo = new PDO(DB_DRIVER . ':host=' . DB_HOST . ';dbname=' . DB_DATABASE, DB_USER, DB_PWD);
        $slct = $pdo->prepare($str_select);
        $slct->bindParam(':c', $cod_crianca, PDO::PARAM_INT);
        // echo "slct->bind<br>";
        $slct->execute();
        //echo "slct->execute<br>";
        if ($slct->rowCount() > 0) {
            $rs_json = array();
            if($alias){
                $cont = 0;
                while ($row = $slct->fetch(PDO::FETCH_BOTH)) {
                    $array_aux = array();
                    for($i=0; $i<sizeof($array_fiedls); $i++){
                        if($i==sizeof($array_fiedls)-1){
                            $array_aux[$array_fiedls[$i]]= "http://harpia.profalexaraujo.com.br/files/".$row[$i];
                        }else{
                            $array_aux[$array_fiedls[$i]] = $row[$i];
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
        echo "else";
    }
} catch(Exception $e){
    echo $e->getMessage()."<br>";
    // echo "-2";
    print_r($e->getTraceAsString());
}

?>