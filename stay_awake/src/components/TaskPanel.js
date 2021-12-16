import React, {useState} from 'react';
import Task from './Task';
import "../style/TaskPanel.css";
import { func } from 'prop-types';

function TaskPanel(props) {
    
	const [tasks, updateTasks] = useState([]);
    tasks.length += 1

    const drop = e => {
        e.preventDefault();
        const card_id= e.dataTransfer.getData('card_id');
        const card = document.getElementById(card_id);
        card.style.display="block";
        e.target.appendChild(card);
    }
    const dragOver = e => {
        e.preventDefault();

    }

    function update() {
        var requestOptions = {
            method: 'GET'          };
          
          fetch("http://localhost:3000/api/v1/Tasks", requestOptions)
            .then(response => response.text())
            .then(result =>  updateTasks(result))
            .catch(error => console.log('error', error));
    }

	function expandTaskPanel() {

        update()
		document.getElementById("container").style.width = "350px";
        document.getElementById("expandButton").style.display = "none";
	}

    function collapseTaskPanel() {
        document.getElementById("expandButton").style.display = "initial";
		document.getElementById("container").style.width = "0";
        closeForm();
	}

    function openForm() {
        document.getElementById("popupForm").style.display = "block";

        document.getElementById("title").value = null;
        document.getElementById("startDate").value = null;
        document.getElementById("endDate").value = null;
        document.getElementById("location").value = null;
        document.getElementById("description").value = null;
        document.getElementById("priority").value = null;
      }
      
      function closeForm() {
        document.getElementById("popupForm").style.display = "none";
      }

    function createTask() {
        let newTask = {"title": document.getElementById("title").value,
                       "startDate": new Date(document.getElementById("startDate").value),
                       "endDate": new Date(document.getElementById("endDate").value),
                       "location": document.getElementById("location").value,
                       "description": document.getElementById("description").value,
                       "priority": document.getElementById("priority").value
                    }

        console.log("New task:");
        console.log(newTask);
                
        var urlencoded = new URLSearchParams();
            urlencoded.append(newTask.title , "Seconde tâche");
            urlencoded.append("description", "Une desription");
            urlencoded.append("priority", "1");
            var requestOptions = {
                method: 'POST',
                body: urlencoded,
                redirect: 'follow'
            };
            fetch("http://localhost:3000/api/v1/createTask", requestOptions)
                .then(response => response.text())
                .then(result => updateTasks(result))
                .catch(error => console.log('error', error));

        if (!!newTask.title && !!newTask.startDate) {
            closeForm();
            update()
        }

        else {
            alert("Merci de renseigner au minimum le titre et les dates de la tâche")
        }
    }

	return(
        <>
            <div className="taskPanel" id={props.id} onDrop={drop} onDragOver={dragOver} className={props.className}>
                <button id="expandButton" className="expandButton" onClick={() => expandTaskPanel()}>&#60;</button>
                <div id="container" className="container">
                    <button className="closeButton" onClick={() => collapseTaskPanel()}>&times;</button>
                    { 
                        <Task title="RDV dentiste" duration="3" id="card-1" className="card" draggable="true"> {props.children} </Task>
                    }
                    {!tasks.length &&
                        <div className="emptyDiv">Aucunes tâches à afficher</div>    
                    }
                    <button className="createTaskButton" onClick={() => openForm()}>+</button>
                </div>
            </div>
            <div className="popupForm" id="popupForm">
                <div className="formContainer">
                    <h1 className="title"> Création d'une tâche</h1>

                    <label for="title">Titre</label>
                    <input type="text" placeholder="Nom de la tâche" id="title" required/>

                    <label for="description">Description</label>
                    <input type="text" placeholder="Description" id="description"/>

                    <label for="startDate">Date de début</label>
                    <input type="datetime-local" id="startDate" required/>

                    <label for="endDate">Date de fin</label>
                    <input type="datetime-local" id="endDate" required/>

                    <label for="location">Destination</label>
                    <input type="text" id="location"/>

                    <label for="priority">Priorité de la tâche</label>
                    <input type="number" min={1} max={3} id="priority"/>

                    <button type="submit" className="addButton" onClick={() => createTask()}>Ajouter</button>
                    <button type="button" className="cancelButton" onClick={() => closeForm()}>Annuler</button>
                </div>
            </div>
        </>
	);
};

export default TaskPanel;