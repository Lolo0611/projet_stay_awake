var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// Location point model
const pointSchema = new mongoose.Schema({
  type: {
    type: String,
    enum: ['Point'],
    required: true
  },
  coordinates: {
    type: [Number],
    required: true
  }
});

const TaskSchema = new Schema({
  Title : String,
  Date : Date,
  Duration : Date,
  Location: pointSchema,
  Description: String,
  Priority : Number
});

module.exports = mongoose.model('Task', TaskSchema);