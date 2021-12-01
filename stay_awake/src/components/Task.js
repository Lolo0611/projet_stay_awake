import React, {useState} from 'react';
import axios from 'axios';
import "../style/Task.css";

function Task({task}) {

    const [taskDetailPane, updateTaskDetailPane] = useState(false)

	function showTaskDetail() {
		updateTaskDetailPane(!taskDetailPane);
	}

    function updateTask() {

        // Need to get input from user

        // axios ({
        //     method: 'put',
        //     url: '/api/v1/updateTask/' + String(task.id),
        //     data: dataToUpdate
        //     })
        //     .then(() => {
        //         alert("La tâche à bien été mise à jour.")
        //     })
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