import React, { useState } from 'react'
import '../style/Task.css'
import fleche from '../assets/fleche.png'


function Task(){
    const [taskbar, setTaskbar] = useState(false);
    const showTaskBar = () => setTaskbar(!taskbar);
    return(
        <div className="slider">
        <div onClick={showTaskBar} className={taskbar? 'taskbar' : 'taskbar-hidden' }>
            <img className="fleche" src={fleche} alt="fleche"/>
            <p> Ici gestion des tâches </p> <p> Ici gestion des tâches </p>
        </div>
        </div>
    )
}

export default Task ;