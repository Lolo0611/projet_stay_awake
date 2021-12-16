var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const TaskSchema = new Schema({
  title : String,
  day : String,
  hour : String,
  duration : Number,
  location: String,
  description: String,
  priority : Number,
  permanent : Boolean,
  checked : Boolean
});

module.exports = mongoose.model('Task', TaskSchema);