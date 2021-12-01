import React, {useState} from 'react';
import axios from 'axios';
import Task from './Task';
import "../style/TaskPanel.css";

function TaskPanel() {
    
	const [tasks, updateTasks] = useState([]);

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

        if (!!newTask.title && !!newTask.startDate) {

            closeForm();

            axios ({
                method: 'post',
                url: '/api/v1/createTask',
                data: newTask
                })
                .then(() => {
                    updateTasks([...tasks, newTask])
                })

            update()

        }

        else {
            alert("Merci de renseigner au minimum le titre et les dates de la tâche")
        }
    }

    function update() {
        axios
            .get("/api/v1/Tasks")
            .then(response => {
                updateTasks(response.data);
            })
            .catch(err => console.log("Erreur"));
    }

	return(
        <>
            <div className="taskPanel">
                <button id="expandButton" className="expandButton" onClick={() => expandTaskPanel()}>&#60;</button>
                <div id="container" className="container">
                    <button className="closeButton" onClick={() => collapseTaskPanel()}>&times;</button>
                    {!!tasks.length &&
                        tasks.forEach(task => {
                            <Task task={task}/>
                        })
                    }

                    {!tasks.length &&
                        <div className="emptyDiv">Aucunes tâches à afficher</div>
                    }

                    <button className="createTaskButton" onClick={() => openForm()}>+</button>
                </div>
            </div>
            <div className="popupForm" id="popupForm">
                <div className="formContainer">
                    <h1>Création d'une tâche</h1>

                    <label for="title"><b>Titre</b></label>
                    <input type="text" placeholder="Nom de la tâche" id="title" required/>

                    <label for="description"><b>Description</b></label>
                    <input type="text" placeholder="Description" id="description"/>

                    <label for="startDate"><b>Date de début</b></label>
                    <input type="datetime-local" id="startDate" required/>

                    <label for="endDate"><b>Date de fin</b></label>
                    <input type="datetime-local" id="endDate" required/>

                    <label for="location"><b>Destination</b></label>
                    <input type="text" id="location"/>

                    <label for="priority"><b>Priorité de la tâche</b></label>
                    <input type="number" min={1} max={3} id="priority"/>

                    <button type="submit" className="addButton" onClick={() => createTask()}>Ajouter</button>
                    <button type="button" className="cancelButton" onClick={() => closeForm()}>Annuler</button>
                </div>
            </div>
        </>
	);
};

export default TaskPanel;