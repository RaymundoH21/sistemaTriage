<?PHP
$hostname_localhost="localhost";
$database_localhost="sistematriage";
$username_localhost="root";
$password_localhost="";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	 
	  $NoPaciente=$_POST["NoPaciente"];
	  $Usuario=$_POST["Usuario"];
	  $Color=$_POST["Color"];
	  $Estado=$_POST["Estado"];	
	  $Fecha=$_POST["Fecha"];
	  $Destino=$_POST["Destino"];
	  $Ambulancia=$_POST["Ambulancia"];
	  $Cama=$_POST["Cama"];
	  $Nombre=$_POST["Nombre"];
	  $Sexo=$_POST["Sexo"];
	  $Edad=$_POST["Edad"];
	  $Lesiones=$_POST["Lesiones"];
	  
	  
	  $sql="INSERT INTO modificacionescolor(NoPaciente, Usuario, Color, Estado, Fecha, Destino, Ambulancia, Cama, Nombre, Sexo, Edad, Lesiones) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	  $stm=$conexion->prepare($sql);
	  $stm->bind_param('isssssssssss',$NoPaciente,$Usuario,$Color,$Estado,$Fecha,$Destino,$Ambulancia,$Cama,$Nombre,$Sexo,$Edad,$Lesiones);
	  
	  if($stm->execute()){
		  echo "Registra";
	  }else{
		  echo "NoRegistra";
	  }
	  mysqli_close($conexion);
	  
?>
