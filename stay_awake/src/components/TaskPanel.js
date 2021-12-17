import React, {useState} from 'react';
import Task from './Task';
import "../style/TaskPanel.css";

function TaskPanel(props) {

    const [tasks, setTasks] = useState([]);

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
        const requestOptions = {method: 'GET'};
          
          fetch("http://localhost:3000/api/v1/Tasks", requestOptions)
            .then(response => response.json())
            .then(result => setTasks(result))
            .catch(error => console.log('error', error))
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

        if (!!newTask.title && !!newTask.duration) {
            closeForm();

            let urlencoded = new URLSearchParams();
                urlencoded.append("title", String(document.getElementById("title").value));
                urlencoded.append("duration", Number(document.getElementById("duration").value));
                urlencoded.append("description", String(document.getElementById("description").value));
                urlencoded.append("priority", Number(document.getElementById("priority").value));
                urlencoded.append("permanent", Boolean(document.getElementById("permanent").value));

                let requestOptions = {
                    method: 'POST',
                    body: urlencoded,
                    redirect: 'follow'
                };
                
                fetch("http://localhost:3000/api/v1/createTask", requestOptions)
                    .then(result => result.json())
                    .then(result => {setTasks([...tasks, result])})
                    .catch(error => console.log('error', error));
                
            update();
        }

        else {
            alert("Merci de renseigner au minimum le titre et la durée de la tâche")
        }

    }
    
	return(
        <>
            <div className="taskPanel" id={props.id} onDrop={drop} onDragOver={dragOver} className={props.className}>
                <button id="expandButton" className="expandButton" onClick={() => expandTaskPanel()}>&#60;</button>
                <div id="container" className="container">
                    <button className="closeButton" onClick={() => collapseTaskPanel()}>&times;</button>
                    <div id="taskContainer" className="taskContainer">
                        {(tasks && tasks.length > 0) 
                        ?
                            tasks.map((task, index) => {
                                return <Task key={index} task={task} id={index} items={props.items} setItems={props.setItems} className="taskCard" draggable="true"/>
                            })
                        :
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