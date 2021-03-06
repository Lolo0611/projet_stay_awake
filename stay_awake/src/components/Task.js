import React, {useState, useEffect} from 'react';
import {guid} from 'react-agenda';
import "../style/Task.css";

function Task(props) {
    const task = props.task
    const items = props.items

    const [taskDetailPane, updateTaskDetailPane] = useState(false)

    const dragStart = e => {
        const target = e.target;
        e.dataTransfer.setData('card_id', target.id);
        setTimeout(() => {
            target.style.display = "none";
        }, 0);
    }

    const dragOver = e => {
        e.stopPropagation();

        // Multiplies to infinity
        let item = {
            _id : guid(),
            idTask : task._id,
            name : task.title,
            startDateTime : "",
            endDateTime : "",
            duration: task.duration,
        }

        props.setItems([...items, item])
    }

    useEffect(() => {
        // Need to update dates here
    })

	function showTaskDetail() {
		updateTaskDetailPane(!taskDetailPane);
	}

    function openForm() {
        document.getElementById("popupFormUpdate").style.display = "block";

        document.getElementById("title").value = task.title;
        document.getElementById("duration").value = task.duration;
        document.getElementById("location").value = task.location;
        document.getElementById("description").value = task.description;
        document.getElementById("priority").value = task.priority;
        document.getElementById("permanent").value = task.permanent;
    }
      
    function closeForm() {
        document.getElementById("popupFormUpdate").style.display = "none";
    }

    // NEED TO DETECT WHAT FIELDS CHANGED
    function updateTask() {
        closeForm();

        let urlencoded = new URLSearchParams();
            urlencoded.append("title", String(document.getElementById("title").value));
            urlencoded.append("duration", Number(document.getElementById("duration").value));
            urlencoded.append("description", String(document.getElementById("description").value));
            urlencoded.append("priority", Number(document.getElementById("priority").value));
            urlencoded.append("permanent", Boolean(document.getElementById("permanent").value));

            let requestOptions = {
                method: 'PUT',
                body: urlencoded,
                redirect: 'follow'
            };

            fetch("http://localhost:3000/api/v1/updateTask/:" + task.id, requestOptions)
                .then(result => {
                    console.log(result)
                    alert("La t??che a ??t?? modifi??e avec succ??s");
                })
                .catch(error => console.log('error', error));
    }

    function direction() {
        // redirect to OSM with corresponding itinerary
        console.log("redirecting to OSM for itinerary")
    }
 
	return(
        <>
            <div id={props.id} className={props.className} draggable={props.draggable} onDragStart={dragStart} onDragOver={dragOver} className="task" onClick={() => showTaskDetail()}>
                <div className='task_content'>

                    <h2 className="title">{task.title}</h2>
                    <p className="location">{task.location}</p>

                    {taskDetailPane &&
                        <div className='details'>
                            <p className="details-content">{task.description}</p>
                            <div className="details-content">Map placeholder</div>
                            <button className="details-button" onClick={() => direction()}>S'y d??placer</button>
                            <button className="details-button" onClick={() => openForm()}>Modifier</button>
                        </div>
                    }

                    <div className="popupFormUpdate" id="popupFormUpdate">
                        <div className="formContainer">
                            <h1 className="form_title"> Modifier une t??che</h1>

                            <label htmlFor="title">Titre</label>
                            <input type="text" placeholder="Nom de la t??che" id="title" required/>

                            <label htmlFor="description">Description</label>
                            <input type="text" placeholder="Description" id="description"/>

                            <label htmlFor="location">Destination</label>
                            <input type="text" placeholder="Destination" id="location"/>

                            <label htmlFor="duration">Dur??e</label>
                            <input type="number" min={0} max={17} step="1" id="duration" required/>

                            <label htmlFor="priority">Priorit?? de la t??che</label>
                            <input type="number" min={1} max={3} id="priority"/>

                            <label htmlFor="permanent">T??che r??currente</label>
                            <input type="checkbox" id="permanent"/>

                            <button type="submit" className="addButton" onClick={() => updateTask()}>Modifier</button>
                            <button type="button" className="cancelButton" onClick={() => closeForm()}>Annuler</button>
                        </div>
                    </div>
                </div>
            </div> 
        </>
	);
};

export default Task;