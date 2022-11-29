<html>
<head><title>login</title></head>
<body>
<h3>login du client </h3>
 ${message} <br/>
<form action="verifLogin" method="GET">
numClient: <!-- <input type="text" name="numClient" /> -->
          <select name="numClient">
              <option>1</option>
              <option>2</option>
          </select>
<br/>
 <input type="submit" value="login" />
</form>
</body>
</html>