<?php
    $con = mysqli_connect("localhost", "root", "", "sistematriage");
    
    $numero = $_POST["numero"];
    $contra = $_POST["contra"];
	
   $statement = mysqli_prepare($con, "SELECT * FROM paramedico WHERE numero = ? AND contra = ?");
    mysqli_stmt_bind_param($statement,"is",$numero,$contra);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement,$numero,$nombre,$apellidop,$apellidom,$edad,$contra,$confirmar,$imagen,$ruta_imagen);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
		$response["numero"] = $numero;
        $response["nombre"] = $nombre;
		$response["apellidop"] = $apellidop;
		$response["apellidom"] = $apellidom;
        $response["edad"] = $edad;
		$response["contra"] = $contra;
		$response["confirmar"] = $confirmar;
    }
    
    echo json_encode($response);
	
?>