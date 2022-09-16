<?php
    $con = mysqli_connect("localhost", "root", "", "bomberos");
    
    $numero = $_POST["numero"];
    $contra = $_POST["contra"];
	
   $statement = mysqli_prepare($con, "SELECT * FROM bombero WHERE numero = ? AND contra = ?");
    mysqli_stmt_bind_param($statement,"is",$numero,$contra);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$numero,$contra,$nombre);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
		$response["numero"] = $numero;
		$response["contra"] = $contra;
		$response["nombre"] = $nombre;
    }
    
    echo json_encode($response);
	
?>