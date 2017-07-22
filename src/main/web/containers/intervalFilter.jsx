import React from 'react';
import {connect} from 'react-redux';
import {MenuItem, DropdownButton} from 'react-bootstrap';
import {setFilterInterval} from '../actions/filterActions';

export const intervals = ['weekly', 'monthly', 'yearly'];

function IntervalFilter(props) {
	return {
		render() {
			return (
				<DropdownButton title={'interval'} id={'interval-selector'} onSelect={props.setFilterInterval}>
					{intervals.map((interval, index) => <MenuItem eventKey={index}>{interval}</MenuItem>)}
				</DropdownButton>
			);
		}
	};
}

IntervalFilter.propTypes = {
};

const stateToProps = state => ({});

const dispatchToProps = dispatch => ({
	setFilterInterval: key => dispatch(setFilterInterval(intervals[key]))
});

export default connect(stateToProps, dispatchToProps)(IntervalFilter);