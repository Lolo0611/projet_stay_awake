import React from 'react';
import {ReactAgenda} from 'react-agenda';

require('moment/locale/fr.js'); // this is important for traduction purpose

var colors = {
    'color-1': "rgba(102, 195, 131 , 1)",
    "color-2": "rgba(242, 177, 52, 1)",
    "color-3": "rgba(235, 85, 59, 1)"
}
var now = new Date();

const drop = e => {
    e.preventDefault();
    const card_id= e.dataTransfer.getData('card_id');
    const card = document.getElementById(card_id);
    card.style.display="block";
    e.target.appendChild(card);
}

const dragOver = e => {
    e.preventDefault();
}

export default class Agenda extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            items: props.items,
            selected: [],
            cellHeight: 30,
            showModal: true,
            locale: "fr",
            rowsPerHour: 1,
            numberOfDays: 1,
            endAtTime: 23,
            startAtTime: 6,
            startDate: new Date(),
        }
        this.handleCellSelection = this.handleCellSelection.bind(this)
        this.handleItemEdit = this.handleItemEdit.bind(this)
        this.handleRangeSelection = this.handleRangeSelection.bind(this)
    }

    componentDidMount() {
        let startDateTime = new Date(String(this.handleCellSelection).replace('-', '/').replace('T', ' '))
        this.state.items.startDateTime = startDateTime
        this.state.items.endDateTime = startDateTime.setHours(startDateTime.getHours() + this.state.items.duration)

        delete this.state.items.duration
    }

    componentDidUpdate() {
        // Do something
    }

    handleCellSelection(item) {
        console.log('handleCellSelection', item)
    }
    handleItemEdit(item) {
        console.log('handleItemEdit', item)
    }
    handleRangeSelection(item) {
        console.log('handleRangeSelection', item)
    }

    render() {
        return ( 
            <div
            onDrop={drop}
            onDragOver={dragOver}>
            <ReactAgenda
            id={this.state.id}
            className={this.state.className}
            ReactAgenda minDate = { now }
            maxDate = { new Date(now.getFullYear(), now.getMonth() + 3) }
            disablePrevButton = { false }
            startDate = { this.state.startDate }
            cellHeight = { this.state.cellHeight }
            endAtTime = { this.state.endAtTime }
            startAtTime = { this.state.startAtTime }
            locale = { this.state.locale }
            items = { this.state.items }
            numberOfDays = { this.state.numberOfDays }
            rowsPerHour = { this.state.rowsPerHour }
            itemColors = { colors }
            autoScale = { false }
            fixedHeader = { true }
            onItemEdit = { this.handleItemEdit.bind(this) }
            onCellSelect = { this.handleCellSelection.bind(this) }
            onRangeSelection = { this.handleRangeSelection.bind(this) }
            /> 
                {this.state.children}
            </div>
        );
    }
}