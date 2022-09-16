<?PHP
$hostname_localhost="localhost";
$database_localhost="sistematriage";
$username_localhost="root";
$password_localhost="";


	if(isset($_GET["NoPaciente"])){
		$NoPaciente=$_GET["NoPaciente"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$sql="DELETE FROM paciente WHERE NoPaciente= ? ";
		$stm=$conexion->prepare($sql);
		$stm->bind_param('i',$NoPaciente);
			
		if($stm->execute()){
			echo "elimina";
		}else{
			echo "noElimina";
		}
		
		mysqli_close($conexion);
	}
	else{
		echo "noExiste";
	}

?>