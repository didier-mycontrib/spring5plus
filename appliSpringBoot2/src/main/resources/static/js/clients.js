sessionStorage.setItem("authToken",null);

function parseJwt (token) {
    let base64Url = token.split('.')[1];
    let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    let jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return jsonPayload;
    //return JSON.parse(jsonPayload);
};

var traiterReponse = function(response) {
	//response ici au format "json string"
	let zoneResultat = document.getElementById("spanRes");
	let jsClient = JSON.parse(response);
	zoneResultat.innerHTML = jsClient.numero + " " + jsClient.prenom + " " + jsClient.nom; //...
}

function onSearchClient() {
	let zoneNumClient = document.getElementById("txtNumClient");
	let numClient = zoneNumClient.value;
	console.log("numClient=" + numClient);
	var urlWsGet = "./rest/bank-api/client/" + numClient;
	makeAjaxGetRequest(urlWsGet, traiterReponse ); //non bloquant (asynchrone)
	//....
}

function doAjout(){
	
	var prenom = document.getElementById("txtPrenom").value;
	var nom = document.getElementById("txtNom").value;
	var adresse = document.getElementById("txtAdresse").value;
	var email = document.getElementById("txtEmail").value;
	
	var url = "./rest/bank-api/client"

    var callback = function(data){
	   console.log("success data=" + data);
       var message ="donnees sauvegardees cote serveur=" + data;
       document.getElementById("spanMessage").innerHTML="<b>"+message+"</b>";
    }

    var errCallback = function(data){
	   console.log("erreur=" + data);
       var message = (JSON.parse(data)).message;
       document.getElementById("spanMessage").innerHTML="<b>"+message+"</b>";
    }

    var jsCustomerObject = { prenom : prenom , nom : nom , adresse : adresse , email : email };
    var jsonData = JSON.stringify(jsCustomerObject);

    makeAjaxPostRequest(url,jsonData,callback,errCallback) ;

	
}

function doLogin(){

	var username = document.getElementById("txtUsername").value;
	var password = document.getElementById("txtPassword").value;

	
	var url = "./rest/api-login/public/login"

    var callback = function(data){
	   console.log("success data=" + data);
       var jwtToken = (JSON.parse(data)).token;
       //tokenGlobal=jwtToken;
       sessionStorage.setItem("authToken",jwtToken);
       var message ="reponse login=" + data + " payload token=" + parseJwt(jwtToken);
       document.getElementById("spanMessageLogin").innerHTML="<b>"+message+"</b>";
    }

    var errCallback = function(data){
	   console.log("erreur=" + data);
       var message = (JSON.parse(data)).message;
       sessionStorage.setItem("authToken",null);
       document.getElementById("spanMessageLogin").innerHTML="<b>"+message+"</b>";
    }

    var jsLoginRequestObject = {username , password};
    var jsonData = JSON.stringify(jsLoginRequestObject);

	makeAjaxPostRequest(url,jsonData,callback,errCallback) ;
	
}