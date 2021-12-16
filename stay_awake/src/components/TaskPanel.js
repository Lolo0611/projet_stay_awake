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
                        duration: Number(document.getElementById("duration").value),
                        location: String(document.getElementById("location").value),
                        description: String(document.getElementById("description").value),
                        priority: Number(document.getElementById("priority").value),
                        permanent: Boolean(document.getElementById("permanent").value)
                        }

        console.log("New task:");
        console.log(newTask);
        console.log(JSON.stringify(newTask));

        if (!!newTask.title && !!newTask.duration) {

            closeForm();

            const config = {headers: {'Content-Type': 'application/json'}}

            axios.post({
                url: "http://localhost:3000/api/v1/createTask",
                data: JSON.stringify(newTask),
                config: config,
                })
                .then(() => {
                    updateTasks([...tasks, newTask]);
                    alert("Tâche créée avec succès");
                })

            update()
        }

        else {
            alert("Merci de renseigner au minimum le titre et la durée de la tâche")
        }
    }

    async function update() {
        axios.get("http://localhost:3000/api/v1/Tasks")
            .then(response => {
                console.log("Tasks:")
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
                    <div className="taskContainer">
                        {!!tasks.length &&
                            tasks.forEach(task => {
                                <Task task={task}/>
                            })
                        }

                        {!tasks.length &&
                            <div className="emptyDiv">Aucunes tâches à afficher</div>
                        }
                    </div>

                    <button className="createTaskButton" onClick={() => openForm()}>+</button>
                </div>
            </div>
            <div className="popupForm" id="popupForm">
                <div className="formContainer">
                    <h1 className="title"> Création d'une tâche</h1>

                    <label htmlFor="title">Titre</label>
                    <input type="text" placeholder="Nom de la tâche" id="title" required/>

                    <label htmlFor="description">Description</label>
                    <input type="text" placeholder="Description" id="description"/>

                    <label htmlFor="location">Destination</label>
                    <input type="text" placeholder="Destination" id="location"/>

                    <label htmlFor="duration">Durée (min)</label>
                    <input type="number" min={0} step="15" id="duration" required/>

                    <label htmlFor="priority">Priorité de la tâche</label>
                    <input type="number" min={1} max={3} id="priority"/>

                    <label htmlFor="permanent">Tâche récurrente</label>
                    <input type="checkbox" id="permanent"/>

                    <button type="submit" className="addButton" onClick={() => createTask()}>Ajouter</button>
                    <button type="button" className="cancelButton" onClick={() => closeForm()}>Annuler</button>
                </div>
            </div>
        </>
	);
};

export default TaskPanel;