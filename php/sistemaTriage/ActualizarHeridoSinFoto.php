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
	$Destino=$_POST["Destino"];
	$Ambulancia=$_POST["Ambulancia"];
	$Cama=$_POST["Cama"];
	$Nombre=$_POST["Nombre"];
	$Sexo=$_POST["Sexo"];
	$Edad=$_POST["Edad"];
	$Lesiones=$_POST["Lesiones"];


	$sql="UPDATE paciente SET Color= ? , Usuario= ?, Estado= ?, Fecha= ?, Destino= ?, Ambulancia=?, Cama=?, Nombre=?, Sexo=?, Edad=?, Lesiones=? WHERE NoPaciente= ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('sssssssssssi',$Color,$Usuario,$Estado,$Fecha,$Destino,$Ambulancia,$Cama,$Nombre,$Sexo,$Edad,$Lesiones,$NoPaciente);
		
	if($stm->execute()){
		echo "actualiza";
	}else{
		echo "noActualiza";
	}
	mysqli_close($conexion);
?>