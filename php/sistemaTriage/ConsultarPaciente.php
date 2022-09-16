<?PHP
$hostname_localhost ="localhost";
$database_localhost ="sistematriage";
$username_localhost ="root";
$password_localhost ="";

$json=array();

	if(isset($_GET["NoPaciente"])){
		$NoPaciente=$_GET["NoPaciente"];
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select Ubicacion, Color, Estado, Usuario, Fecha, Foto, Ambulancia, Destino, Cama, Nombre, Sexo, Edad, Lesiones from paciente where NoPaciente= '{$NoPaciente}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$result["Ubicacion"]=$registro['Ubicacion'];
			$result["Color"]=$registro['Color'];
			$result["Estado"]=$registro['Estado'];
			$result["Usuario"]=$registro['Usuario'];
			$result["Fecha"]=$registro['Fecha'];			
		
			$result["imagen"]=base64_encode($registro['Foto']);
			$result["Ambulancia"]=$registro['Ambulancia'];
			$result["Destino"]=$registro['Destino'];
			$result["Cama"]=$registro['Cama'];
			$result["Nombre"]=$registro['Nombre'];
			$result["Sexo"]=$registro['Sexo'];
			$result["Edad"]=$registro['Edad'];
			$result["Lesiones"]=$registro['Lesiones'];
			$json['paciente'][]=$result;
		
		}else{
			
			$resultar["Ubicacion"]='no registra';
			$resultar["Color"]='no registra';
			$resultar["Estado"]='no registra';
			$resultar["Usuario"]='no registra';
			$resultar["Fecha"]='no registra';

			$resultar["imagen"]='no registra';
			$resultar["Ambulancia"]='no registra';
			$resultar["Destino"]='no registra';
			$resultar["Cama"]='no registra';
			$resultar["Nombre"]='no registra';
			$resultar["Sexo"]='no registra';
			$resultar["Edad"]='no registra';
			$resultar["Lesiones"]='no registra';
			$json['paciente'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['paciente'][]=$resultar;
		echo json_encode($json);
	}
?>