const functions = require('../functions/task')

function createTask(req, res) {
    let Task = require('../models/task');

    let newTask = Task ({
        title: req.body.title,
        day : req.body.day,
        hour : req.body.hour,
        duration : req.body.duration,
        location : req.body.location,
        description : req.body.description,
        priority : req.body.priority,
        permanent : req.body.permanent,
        checked : false
    });
  
    newTask.save()
    .then((savedTask) => {

        res.json(savedTask);
            
    }, (err) => {
        res.status(400).json(err)
    });

}

function readTasks(req, res) {

    let Task = require("../models/task");

    Task.find({day: undefined}, null, {sort: {priority: 1}})
    .populate('Location')
    .then((tasks) => {
        res.status(200).json(functions.cleanTasks(tasks));
    }, (err) => {
        res.status(500).json(err);
    });
 }

function readTasksByDay(req, res) {
    let Task = require("../models/task");

    Task.find({day : req.params.day}, null, {sort: {hour: 1}})
    .populate('location')
    .then((tasks) => {
        res.status(200).json(tasks);
    }, (err) => {
        res.status(500).json(err);
    });
}

function readTask(req, res) {

    let Task = require("../models/task");

    Task.findById({_id : req.params.id})
    .populate('location')
    .then((task) => {
        res.status(200).json(task);
    }, (err) => {
        res.status(500).json(err);
    });
 }

function updateTask(req, res) {

    let Task = require("../models/task");

    let newFields = {};

    //Allows to only update the given fields
    if(req.body.title) newFields.title = req.body.title;
    if(req.body.day) newFields.day = req.body.day;
    if(req.body.hour) newFields.hour = req.body.hour;
    if(req.body.duration) newFields.duration = req.body.duration;
    if(req.body.location) newFields.location = req.body.location;
    if(req.body.description) newFields.description = req.body.description;
    if(req.body.priority) newFields.priority = req.body.priority;
    if(req.body.checked) newFields.checked = req.body.checked;

    Task.findByIdAndUpdate(
        {_id: req.params.id}, 
        newFields,
        {new : false})
    .then((updatedTask) => {
        res.status(200).json(updatedTask);
    }, (err) => {
        res.status(500).json(err);
    });
}

function deleteTask(req, res) {

    let Task = require("../models/task");

    Task.findOneAndRemove({_id : req.params.id})
    .then((deletedTask) => {
        res.status(200).json(deletedTask);
    }, (err) => {
        res.status(500).json(err);
    });
 }

module.exports.create = createTask;
module.exports.reads = readTasks;
module.exports.readsByDay = readTasksByDay;
module.exports.read = readTask;
module.exports.delete = deleteTask;
module.exports.update = updateTask;