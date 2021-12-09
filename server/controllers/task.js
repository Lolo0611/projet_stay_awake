function createTask(req, res) {
    let Task = require('../models/task');

    let startDate = req.body.startDate;
    let endDate = req.body.endDate;

    // If no endDate, adds 1 hour
    if (!req.body.endDate && startDate) {
        endDate = startDate
        endDate.setHours(startDate.getHours() + 1);
    }

    let newTask = Task ({
        title: req.body.title,
        startDate : startDate,
        endDate : endDate,
        location : req.body.location,
        description : req.body.description,
        priority : req.body.priority
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
    if(req.body.startDate) newFields.date = req.body.startDate;
    if(req.body.endDate) newFields.duration = req.body.endDate;
    if(req.body.location) newFields.location = req.body.location;
    if(req.body.description) newFields.description = req.body.description;
    if(req.body.priority) newFields.priority = req.body.priority;

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
module.exports.update = updateTask;