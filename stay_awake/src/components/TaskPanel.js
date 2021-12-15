import React, {useState} from 'react';
import axios from 'axios';
import { stringify } from 'querystring';
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
        document.getElementById("date").value = null;
        document.getElementById("duration").value = null;
        document.getElementById("location").value = null;
        document.getElementById("description").value = null;
        document.getElementById("priority").value = null;
        document.getElementById("permanent").value = null;
      }
      
      function closeForm() {
        document.getElementById("popupForm").style.display = "none";
      }

    function createTask() {
        let newTask = {title: String(document.getElementById("title").value),
                        date: new Date(String(document.getElementById("date").value)).toLocaleDateString(),
                        hour: new Date(String(document.getElementById("date").value)).toLocaleTimeString(),
                        duration: Number(document.getElementById("duration").value),
                        location: String(document.getElementById("location").value),
                        description: String(document.getElementById("description").value),
                        priority: Number(document.getElementById("priority").value),
                        permanent: Boolean(document.getElementById("permanent").value)
                        }

        console.log("New task:");
        console.log(newTask);

        if (!!newTask.title && !!newTask.date) {

            closeForm();

            const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}

            // Still not working somehow
            // Try formData format even though i'm sus 'bout it
            axios
                .post("http://localhost:3000/api/v1/createTask", newTask, config)
                .then(() => {
                    updateTasks([...tasks, newTask]);
                    alert("Tâche créée avec succès");
                })

            update()

        }

        else {
            alert("Merci de renseigner au minimum le titre et les dates de la tâche")
        }
    }

    function update() {
        axios.get("http://localhost:3000/api/v1/Tasks")
            .then(response => {
                console.log("tasks")
                console.log(response.data)
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
                    <h1 className="title"> Création d'une tâche</h1>

                    <label for="title">Titre</label>
                    <input type="text" placeholder="Nom de la tâche" id="title" required/>

                    <label for="description">Description</label>
                    <input type="text" placeholder="Description" id="description"/>

                    <label for="date">Date</label>
                    <input type="datetime-local" id="date" required/>

                    <label for="duration">Durée (min)</label>
                    <input type="number" min={0} step="15" id="duration" required/>

                    <label for="location">Destination</label>
                    <input type="text" id="location"/>

                    <label for="priority">Priorité de la tâche</label>
                    <input type="number" min={1} max={3} id="priority"/>

                    <label for="permanent">Tâche récurrente</label>
                    <input type="checkbox" id="permanent"/>

                    <button type="submit" className="addButton" onClick={() => createTask()}>Ajouter</button>
                    <button type="button" className="cancelButton" onClick={() => closeForm()}>Annuler</button>
                </div>
            </div>
        </>
	);
};

export default TaskPanel;