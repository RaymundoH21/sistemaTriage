<?PHP
$hostname_localhost ="localhost";
$database_localhost ="sistematriage";
$username_localhost ="root";
$password_localhost ="";

$json=array();
//$json1=array();
//$json2=array();
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select NoPaciente,Ubicacion,Color,Estado,Usuario,Fecha,Foto,RutaFoto,Latitud,Longitud,Altitud,Ambulancia from paciente where Estado='En espera' or Estado='Trasladando' ORDER BY FIELD (Color,'Rojo','Amarillo','Verde','Negro')";
		$resultado=mysqli_query($conexion,$consulta);

	
		while($registro=mysqli_fetch_array($resultado)){
			
			$result["NoPaciente"]=$registro['NoPaciente'];
			$result["Ubicacion"]=$registro['Ubicacion'];
			$result["Color"]=$registro['Color'];
			$result["Estado"]=$registro['Estado'];
			$result["Usuario"]=$registro['Usuario'];

			$result["Foto"]=base64_encode($registro['Foto']);
			$result["RutaFoto"]=$registro['RutaFoto'];
			$result["Latitud"]=$registro['Latitud'];
			$result["Longitud"]=$registro['Longitud'];
			$result["Altitud"]=$registro['Altitud'];
			$result["Ambulancia"]=$registro['Ambulancia'];

            $json['paciente'][]=$result;
		
		}//*/


        mysqli_close($conexion);
        echo json_encode($json);
        
?>