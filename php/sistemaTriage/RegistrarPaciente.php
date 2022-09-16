<?PHP
$hostname_localhost="localhost";
$database_localhost="sistematriage";
$username_localhost="root";
$password_localhost="";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	 
	  $Ubicacion=$_POST["Ubicacion"];
	  $Color=$_POST["Color"];
	  $Usuario=$_POST["Usuario"];
	  $Estado=$_POST["Estado"];	
	  $Fecha=$_POST["Fecha"];
	  $imagen=$_POST["imagen"];
	  $Latitud=$_POST["Latitud"];
	  $Longitud=$_POST["Longitud"];
	  $Altitud=$_POST["Altitud"];
	  
	  $path ="imagenes/$Color.jpg";
	  
	  //$url ="http://$hostname_localhost/BDremota/$path"
	  $url = "imagenes/".$Color.".jpg";
	  
	  file_put_contents($path,base64_decode($imagen));
	  $bytesArchivo=file_get_contents($path);

	  
	  
	  $sql="INSERT INTO paciente(Ubicacion, Color, Usuario, Estado, Fecha, Foto, RutaFoto, Latitud, Longitud, Altitud) VALUES (?,?,?,?,?,?,?,?,?,?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('ssssssssss',$Ubicacion,$Color,$Usuario,$Estado,$Fecha,$bytesArchivo,$url,$Latitud,$Longitud,$Altitud);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "NoRegistra";
	  }
	  mysqli_close($conexion);
	  
?>
