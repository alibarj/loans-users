//This function collects the form's data, arrange it in JSON format, and post it to server using an Ajax request
function setUserInfo(){

	let inputs = document.getElementsByClassName('form-control');
	let personalJson = {};
    let profilJson = {};
    let evenementJson = {};
    const evenements  = new Array();
    let nbEvs = 1;
    


	for (let input of inputs) {

		switch(input.dataset.group){
			case "personel":
				personalJson[input.name] = input.value;
				break;

			case "profil":
				profilJson[input.name] = input.value;
				break;

			case "evenement":
				if(nbEvs==1){
					feed = {};
					feed[input.name] = input.value;
					nbEvs += 1;
				}

				else if(nbEvs==2){
					feed[input.name] = input.value;
					nbEvs += 1;
				}

				else if (nbEvs==3){
					feed[input.name] = input.value;
					evenements.push(feed);
					nbEvs = 1;
				}
				break;

				default:
					break;
		}
    }

    


    //Stringify converts from json object to string
	//Parse converts string to json object
	//Enclose profil info in a json object with key "profil" and merge it with personal info into one json
	evenementJson = {evenements : evenements}
    profilJson = {profil : profilJson};
	let json = Object.assign({}, personalJson, profilJson, evenementJson);
	json = JSON.stringify(json);

	console.log(json);

	// Send the request to the backend server with the json in the body
	let ajaxRequest = new XMLHttpRequest();
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			if (ajaxRequest.status == 200){
			alert(ajaxRequest.responseText);
				for (let input of inputs) {
					input.value = "";
				}
			}
			else if(ajaxRequest.status == 0){
				alert("Aucune réponse du serveur");
			}
			else{
					alert(ajaxRequest.responseText);

			}
			
		}
	}
	ajaxRequest.open('POST', "http://localhost:8080/loans_managers/users/");
	ajaxRequest.setRequestHeader("Content-type", "application/json");
	ajaxRequest.send(String(json));


}


//This function sends a request to the server to retrieve existing users info, and created an html table to display these info.
function getUsersInfo(){

	//If the button is clicked for the second time, remove the table's rows before addind them again
	let contentTableBody = document.getElementById("get-table-body-id");
	if (contentTableBody.rows.length){
		while (contentTableBody.firstChild) {
			contentTableBody.removeChild(contentTableBody.lastChild);
		}
	}
	
	//Create an ajax request that communicates with the API, and define what to do when a response is available.
	let ajaxRequest = new XMLHttpRequest();
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			if(ajaxRequest.status == 200){
				
				let input = ajaxRequest.responseText;
					//split the input with newline delimeter (ndjson), filter it (eliminate empty value) and then json parse it.
					let users = input.split('\n').filter(e => e).map(s => JSON.parse(s));

					//If not empty data is recieved, create the body of the tables and fill it with the data.
					if(Array.isArray(users) && users.length){

						let tbody = document.getElementById("get-table-body-id");
						users.forEach(element => {

							//Create HTML elements (rows and content)
							let tr0 = document.createElement('tr');
							let td0 = document.createElement('td');
							let td1 = document.createElement('td');
							let td2 = document.createElement('td');
							let td3 = document.createElement('td');
							let td4 = document.createElement('td');
							let td5 = document.createElement('td');
							let td6 = document.createElement('td');
							let td7 = document.createElement('td');
							let txt0 = document.createTextNode(element.id);
							let txt1 = document.createTextNode(element.nom);
							let txt2 = document.createTextNode(element.prenom);
							let txt3 = document.createTextNode(element.service);
							let txt4 = document.createTextNode(element.profil.code);
							let txt5 = document.createTextNode(element.profil.dateValidite);
							

							//Create delete button
							let deleteButton = document.createElement("button")
							deleteButton.className = "btn btn-outline-danger";
							deleteButton.dataset.id = element.id;
							let deleteIcon = document.createElement("i");
							deleteIcon.className = "bi bi-trash-fill";
							deleteButton.addEventListener("click", deleteUser);

							//Create show more button
							let bt = document.createElement("button");
							let txt6 = document.createTextNode("Afficher ▼");
							bt.className = "btn btn-link";
							bt.addEventListener("click", getEvenements);

							//Create nested table (Evenements)
							let evTable = document.createElement("table");
							evTable.className = "table table-bordered";
							let tr00 = document.createElement('tr');
							let h0 = document.createElement('th');
							h0.innerHTML = "Evenement";
							let h1 = document.createElement('th');
							h1.innerHTML = "Date validité";
							let h2 = document.createElement('th');
							h2.innerHTML = "Habilitation";
							tr00.appendChild(h0);
							tr00.appendChild(h1);
							tr00.appendChild(h2);
							evTable.appendChild(tr00);
							evTable.hidden = true;
							//Create nested table's rows
							element.evenements.forEach(ev =>{
									let tr1 = document.createElement('tr');
									let d0 = document.createElement('td');
									d0.innerHTML = ev.code;
									let d1 = document.createElement('td');
									d1.innerHTML = ev.date;
									let d2 = document.createElement('td');
									d2.innerHTML = ev.nivHabilitation;
									tr1.appendChild(d0);
									tr1.appendChild(d1);
									tr1.appendChild(d2);
									evTable.appendChild(tr1);
							})


							//Append HTML elements to their respecitve parents
							bt.appendChild(txt6);
							td0.appendChild(txt0);
							td1.appendChild(txt1);
							td2.appendChild(txt2);
							td3.appendChild(txt3);
							td4.appendChild(txt4);
							td5.appendChild(txt5);
							td6.appendChild(bt);
							deleteButton.appendChild(deleteIcon);
							td7.appendChild(deleteButton);
							td6.appendChild(evTable);
							tr0.append(td0, td1, td2, td3, td4, td5, td6, td7);
							tbody.appendChild(tr0);
						});
			
				}
			}
			else if(ajaxRequest.status == 0){
				alert("Aucune réponse du serveur");
			}
			else{
				alert(ajaxRequest.responseText);

			}
		}
	}

	ajaxRequest.open('GET', 'http://localhost:8080/loans_managers/users/');
	ajaxRequest.send();
}
		

//This function defines the 'afficher' button's behavior; show (or hide) a user's events table
function "getEvenements"(){

			let nestedTable = this.nextSibling;
			if (nestedTable.hidden){
				nestedTable.hidden = false;
				this.textContent = "Masquer ▲";
			}
			else {
				nestedTable.hidden = true;
				this.textContent = "Afficher ▼ ";
			}

		}


//This function defines the 'delete' button's behavior; delete a user from the database
function deleteUser(){
	let ajaxRequest = new XMLHttpRequest();
	ajaxRequest.onreadystatechange = function(){
		if(ajaxRequest.readyState == 4){
			if (ajaxRequest.status == 200){
			getUsersInfo();
			
			}
			else if(ajaxRequest.status == 0){
				alert("Aucune réponse du serveur");
			}
			else{
				alert(ajaxRequest.responseText);

			}
			
		}
	}
	ajaxRequest.open('DELETE', "http://localhost:8080/loans_managers/users/"+this.dataset.id);
	ajaxRequest.setRequestHeader("Content-type", "application/json");

	//If the user confirms his wish to delete the entry, send the request. If not, delete the request.
	if (confirm('Etes vous sur de vouloir supprimer cet utilisateur?')) {
		ajaxRequest.send();
	}
	else {
		delete ajaxRequest;

	}
}




//This function is used by setEvsForm() to define the number of events the user whishes to add to the form
function setNbEvs(){
	let nbEvs = document.getElementById("inputNbEvs").value;
	if (Number.isInteger(+nbEvs) && nbEvs != "" && nbEvs > 0){
		return nbEvs;
	}
	else{
		return null;
	}
}


//This function deletes the events inputs if they were added with setEvsForm()
function unSetEvsForm(){
	let addButton = document.getElementById("showEvsButton");
	
	let evsFieldSet = document.getElementsByClassName("evs")[0];
	while (evsFieldSet.firstChild) {
   		evsFieldSet.firstChild.remove()
	}

	addButton.innerHTML = "Ajouter des evenements ▼";
	addButton.setAttribute("data-bs-toggle", "modal");
	addButton.click();


}


//This function adds inputs for events to the form when the 'ajouter des evenements' button is clicked
function setEvsForm(){

	let evsFieldSet = document.getElementsByClassName("evs")[0];

	let modal = document.getElementById("evEvsModal");

	//Get the "ajouter des evenements" button to change its behavior afterwards
	let addButton = document.getElementById("showEvsButton");

	//Get the number of evs requested by the user
	let nbEvs = setNbEvs();

	if (nbEvs != null) {
		let legend = document.createElement("legend");
		legend.innerHTML = "Evenements";
		evsFieldSet.appendChild(legend);

		for (let i = 0; i < nbEvs; i++) {
			let div0 = document.createElement("div");
			div0.className = "mx-5 my-5 row";

			let div1 = document.createElement("div");
			div1.className = "col";
			let label1 = document.createElement("label");
			label1.for = "evCodeInput"; label1.className = "form-label"; label1.innerHTML = "Code "+(i+1).toString();
			let input1 = document.createElement("input");
			input1.type = "text"; input1.id = "evCodeInput"+i.toString(); input1.className = "form-control"; input1.dataset.group = "evenement"; input1.name = "code";

			let div2 = document.createElement("div");
			div2.className = "col";
			let label2 = document.createElement("label");
			label2.for = "evDateInput"; label2.className = "form-label"; label2.innerHTML = "Date de validité "+(i+1).toString();
			let input2 = document.createElement("input");
			input2.type = "date"; input2.id = "evDateInput"+i.toString(); input2.className = "form-control"; input2.dataset.group = "evenement"; input2.name = "date";

			let div3 = document.createElement("div");
			div3.className = "col";
			let label3 = document.createElement("label");
			label3.for = "evHabilitationInput"; label3.className = "form-label"; label3.innerHTML = "Niveau d'habilitation "+(i+1).toString();
			let input3 = document.createElement("input");
			input3.type = "text"; input3.id = "evHabilitationInput"+i.toString(); input3.className = "form-control"; input3.dataset.group = "evenement"; input3.name = "nivHabilitation";

			evsFieldSet.appendChild(div0);
			div0.appendChild(div1);
			div0.appendChild(div2);
			div0.appendChild(div3);
			div1.appendChild(label1);
			div1.appendChild(input1);
			div2.appendChild(label2);
			div2.appendChild(input2);
			div3.appendChild(label3);
			div3.appendChild(input3);
		}

		evsFieldSet.style.display = "block";

		//Close the modal
		modal.style.display = "none";
	    modal.className = "modal fade";
	    document.getElementsByClassName("modal-backdrop fade show")[0].style.display = "none";
	    document.body.style.overflow = "visible";

	    //Scroll to this section
	    location.href='#ev-section';

	    //Change button behavior
	    addButton.innerHTML = "Annuler ▲";
	    // addButton.className = "btn btn-link";
	    addButton.setAttribute("data-bs-toggle", "");
	    addButton.onclick = function() { unSetEvsForm(); }
	}
	else {
		document.getElementById("NombreEntierInput").innerHTML ="Le nombre n'est pas entier";
	}
}