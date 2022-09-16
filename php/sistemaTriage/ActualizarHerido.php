<?PHP
$hostname_localhost="localhost";
$database_localhost="sistematriage";
$username_localhost="root";
$password_localhost="";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	$NoPaciente=$_POST["NoPaciente"];
	$Color=$_POST["Color"];
	$Usuario=$_POST["Usuario"];
	$Estado=$_POST["Estado"];
	$Fecha=$_POST["Fecha"];
	$imagen=$_POST["imagen"];
	$Destino=$_POST["Destino"];
	$Ambulancia=$_POST["Ambulancia"];
	$Cama=$_POST["Cama"];
	$Nombre=$_POST["Nombre"];
	$Sexo=$_POST["Sexo"];
	$Edad=$_POST["Edad"];
	$Lesiones=$_POST["Lesiones"];
	

	$path = "imagenes/$Color.jpg";

	//$url = "http://$hostname_localhost/ejemploBDRemota/$path";
	$url = "imagenes/".$Color.".jpg";

	file_put_contents($path,base64_decode($imagen));
	$bytesArchivo=file_get_contents($path);

	$sql="UPDATE paciente SET Color=?, Usuario=?, Estado=?, Fecha=?, Foto=?, RutaFoto=?, Destino=?, Ambulancia=?, Cama=?, Nombre=?, Sexo=?, Edad=?, Lesiones=? WHERE NoPaciente=?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('sssssssssssssi',$Color,$Usuario,$Estado,$Fecha,$bytesArchivo,$url,$Destino,$Ambulancia,$Cama,$Nombre,$Sexo,$Edad,$Lesiones,$NoPaciente);
		
	if($stm->execute()){
		echo "actualiza";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>