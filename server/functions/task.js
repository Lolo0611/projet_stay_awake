const dayjs = require('dayjs')
const customParseFormat = require('dayjs/plugin/customParseFormat')
dayjs.extend(customParseFormat)

function cleanTasks (tasks) {

    let Task = require("../models/task");

    tasks.forEach(task => {
        if(task.day != undefined && (dayjs().isAfter(dayjs(task.day, "YYYY-MM-DD"), 'day'))) {
            task.day = undefined
            task.hour = undefined
            task.duration = undefined

            Task.findByIdAndUpdate(
                {_id: task.id}, 
                {day : undefined, hour : undefined, duration : undefined},
                {new : false})
        }
    });

    return tasks
}

module.exports.cleanTasks = cleanTasks