import React, {useState} from 'react';
import "../style/Task.css";

function Task({task}) {

    const [taskDetailPane, updateTaskDetailPane] = useState(false)

	function showTaskDetail() {
		updateTaskDetailPane(!taskDetailPane);
	}

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
      
            fetch("{{host}}/{{name}}/{{version}}/Tasks", requestOptions)
                .then(response => response.text())
                .then(result => updateTaskDetailPane(result))
                .catch(error => console.log('error', error));
    }

    function direction() {
        // redirect to OSM with corresponding itinerary
        console.log("redirecting to OSM for itinerary")
    }

	return(
		<div className="container" onClick={() => showTaskDetail()}>
            <h1 className="title">{task.title}</h1>
            <p className="date">{task.date}</p>
            <p className="duration">{task.duration}</p>
            {taskDetailPane &&
                <>
                    <p className="description">{task.description}</p>
                    <p className="duration">{task.duration}</p>
                    <div className="map">Map placeholder</div>
                    <button className="itineraryButton" onClick={() => direction()}>S'y déplacer</button>
                    <button className="updateTaskButton" onClick={() => updateTask()}>Modifier</button>
                </>
            }
		</div>
	);
};

export default Task;