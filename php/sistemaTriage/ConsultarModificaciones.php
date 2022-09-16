<?PHP
$hostname_localhost ="localhost";
$database_localhost ="sistematriage";
$username_localhost ="root";
$password_localhost ="";

$json=array();

	if(isset($_GET["NoPaciente"])){
		$NoPaciente=$_GET["NoPaciente"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select NoPaciente, Color, Estado, Usuario, Fecha from modificacionescolor where NoPaciente= '{$NoPaciente}' ORDER BY NoCambio DESC";
		$resultado=mysqli_query($conexion,$consulta);
			
		while($registro=mysqli_fetch_array($resultado)){
			$result["NoPaciente"]=$registro['NoPaciente'];
			$result["Color"]=$registro['Color'];
			$result["Estado"]=$registro['Estado'];
			$result["Usuario"]=$registro['Usuario'];
			$result["Fecha"]=$registro['Fecha'];			
		
			$json['modificacionescolor'][]=$result;
		}
		

		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['modificacionescolor'][]=$resultar;
		echo json_encode($json);
	}
?>