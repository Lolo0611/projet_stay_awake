import './App.css';
import React, {useState, useEffect} from 'react';
import TaskPanel from './components/TaskPanel';
import Navbar from './components/Navbar';
import Agenda from './components/Agenda';

function App() {
    const [items, setItems] = useState([])

    return ( 
        <div className = "App" >
        <div className = "App-header" >
        <h1> Stay Awake </h1>  
        </div>

        <div className = "content" >

            <div className = "NavBar" >
            <Navbar/>
            </div>

            <div className = "content-right">
            <div className = "agenda" >
            <Agenda items={items}/>   
            
            </div>

        <TaskPanel items={items}/>

            </div>
        </div> 
        </div>
        
    );
}

export default App;