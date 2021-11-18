function createTask(req, res) {
    let Task = require('../models/task');
    let newTask = Task ({
        Title: req.body.Title,
        Date : req.body.Date,
        Duration : req.body.Duration,
        Location : req.body.Location,
        Description : req.body.Description,
        Priority : req.body.Priority
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

    Task.find({})
    .populate('Location')
    .then((tasks) => {
        res.status(200).json(tasks);
    }, (err) => {
        res.status(500).json(err);
    });
 }

function readTask(req, res) {

    let Task = require("../models/task");

    Task.findById({_id : req.params.id})
    .populate('Location')
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
    if(req.body.Title) newFields.Title = req.body.Title;
    if(req.body.Date) newFields.Date = req.body.Date;
    if(req.body.Duration) newFields.Duration = req.body.Duration;
    if(req.body.Location) newFields.Location = req.body.Location;
    if(req.body.Description) newFields.Description = req.body.Description;
    if(req.body.Priority) newFields.Priority = req.body.Priority;

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
module.exports.read = readTask;
module.exports.delete = deleteTask;
module.exports.updatePizza = updateTask;