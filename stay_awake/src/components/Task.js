import React, {useState} from 'react';
import "../style/Task.css";

function Task(props) {
    const {title, duration, description, date} = props

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
    }
    

	function showTaskDetail() {
		updateTaskDetailPane(!taskDetailPane);
	}

    //à MAJ c'est pour mettre à jour une tâche
    function updateTask() {
            var urlencoded = new URLSearchParams();
            urlencoded.append("Titre" , "Seconde tâche");
            urlencoded.append("description", "Une desription");
            urlencoded.append("priority", "1");
            var requestOptions = {
                method: 'POST',
                body: urlencoded,
                redirect: 'follow'
            };
            fetch("http://localhost:3000/api/v1/Tasks", requestOptions)
                .then(response => response.text())
                .then(result => updateTaskDetailPane(result))
                .catch(error => console.log('error', error));
    }

    function direction() {
        // redirect to OSM with corresponding itinerary
        console.log("redirecting to OSM for itinerary")
    }

	return(
		<div id={props.id}
        className={props.className} 
        draggable={props.draggable}
        onDragStart={dragStart}
        onDragOver={dragOver} className="task" onClick={() => showTaskDetail()}>
            <h1 className="title">{title}</h1>
            <p className="date">{date}</p>
            <p className="duration">{duration}</p>
            {taskDetailPane &&
                <>
                    <p className="description">{description}</p>
                    <p className="duration">{duration}</p>
                    <div className="map">Map placeholder</div>
                    <button className="itineraryButton" onClick={() => direction()}>S'y déplacer</button>
                    <button className="updateTaskButton" onClick={() => updateTask()}>Modifier</button>
                </>
            }
		</div>
	);
};

export default Task;