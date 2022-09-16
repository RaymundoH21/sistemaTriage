<?PHP
$hostname_localhost ="localhost";
$database_localhost ="sistematriage";
$username_localhost ="root";
$password_localhost ="";

$json=array();
//$json1=array();
//$json2=array();
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$LimitStart=$_POST["LimitStart"];
		$Limit=$_POST["Limit"];
		

		$consulta="select NoPaciente,Ubicacion,Color,Usuario,Estado,Fecha,Foto,RutaFoto,Destino from paciente where Estado='Hospital' or Estado='Alta Médica' or Estado='SEMEFO' or Estado='Recibido' ORDER BY NoPaciente DESC LIMIT $LimitStart,$Limit;";
		$resultado=mysqli_query($conexion,$consulta);

	
		while($registro=mysqli_fetch_array($resultado)){
			
			$result["NoPaciente"]=$registro['NoPaciente'];
			$result["Ubicacion"]=$registro['Ubicacion'];
			$result["Color"]=$registro['Color'];
			$result["Estado"]=$registro['Estado'];
			$result["Usuario"]=$registro['Usuario'];
			$result["Fecha"]=$registro['Fecha'];

			$result["Foto"]=base64_encode($registro['Foto']);
			$result["RutaFoto"]=base64_encode($registro['RutaFoto']);
			$result["Destino"]=$registro['Destino'];

            $json['paciente'][]=$result;
		
		}//*/


        mysqli_close($conexion);
        echo json_encode($json);
        
?>