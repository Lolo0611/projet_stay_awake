import React, {useState} from 'react';
import "../style/Task.css";

function Task({task}) {

    const [taskDetailPane, updateTaskDetailPane] = useState(false)

	function showTaskDetail() {
		updateTaskDetailPane(!taskDetailPane);
	}

    function updateTask() {
        console.log("Update task data")

        // axios ({
        //     method: 'put',
        //     url: 'http://localhost:3000/api/v1/updateTask/' + String(task.id),
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
            <button className="updateTaskButton" onClick={() => updateTask()}>Modifier</button>
            {taskDetailPane &&
                <>
                    <p className="description">{task.description}</p>
                    <div className="map">Map placeholder</div>
                    <button className="itineraryButton" onClick={() => direction()}>S'y déplacer</button>
                </>
            }
		</div>
	);
};

export default Task;