<?php
    $con = mysqli_connect("localhost", "root", "", "sistematriage");
    
	$numero = $_POST["numero"];
    $nombre = $_POST["nombre"];
    $apellidop = $_POST["apellidop"];
	$apellidom = $_POST["apellidom"];
    $edad = $_POST["edad"];
	$contra = $_POST["contra"];
    $confirmar = $_POST["confirmar"];
	$imagen = $_POST["imagen"];
	
	$path ="imagenes/$nombre.jpg";
	
	$url = "imagenes/".$nombre.".jpg";
	  
	  file_put_contents($path,base64_decode($imagen));
	  $bytesArchivo=file_get_contents($path);

    $statement = mysqli_prepare($con, "INSERT INTO paramedico (numero, nombre, apellidop, apellidom, edad, contra, confirmar,imagen, ruta_imagen) VALUES (?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "isssissss", $numero, $nombre, $apellidop, $apellidom, $edad, $contra, $confirmar,$bytesArchivo,$url);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
