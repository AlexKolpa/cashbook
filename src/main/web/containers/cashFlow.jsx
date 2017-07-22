import React from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import IntervalFilter from './intervalFilter';

function CashFlow() {
	return {
		componentDidMount() {

		},
		render() {
			return (
				<div>
					<IntervalFilter/>
					<CashFlowList/>
				</div>
			);
		}
	}
}

CashFlow.propTypes = {
	filter : PropTypes.shape({
		interval : PropTypes.oneOf().isRequired,
		index : PropTypes.number.isRequired
	}).isRequired,
	flow: PropTypes.arrayOf(PropTypes.shape({
		id: PropTypes.number.isRequired,
		date: PropTypes.instanceOf(Date),
		category: PropTypes.object.isRequired,
		subCategory: PropTypes.object,
		description: PropTypes.string,
		cost: PropTypes.number.isRequired
	}))
};

function mapStateToProps(state) {
	const {cashFlow, filter} = state;
	const {
		isFetching,
		flow
	} = cashFlow[filter] || {
		isFetching : true,
		flow : []
	};

	return {
		filter,
		flow,
		isFetching
	}
}

const dispatchToProps = dispatch => ({

});
export default connect(mapStateToProps, dispatchToProps)(CashFlow);
