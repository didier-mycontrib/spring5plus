<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head><title>virement</title></head>
<body>
<h3>virement interne pour le client connecté</h3>
numero_client: ${numClient} <br/>
   <p><b>${message}</b></p>
   <form action="effectuerVirement"  method="POST">
		montant: <input type="text" name="montant" />  <br/>
		numCptDeb: <input type="text" name="numCptDeb" />  <br/>
		numCptCred: <input type="text" name="numCptCred" />  <br/>
     	<input type="submit" value="effectuerVirement" />	
	</form>
	<p> <a href="comptesDuClient" >retour vers comptesDuClient</a> </p>
</body>
</html>