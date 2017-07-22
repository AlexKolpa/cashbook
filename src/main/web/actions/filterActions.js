export const UPDATE_FILTER = 'UPDATE_FILTER';

export function setFilterInterval(interval) {
	return (dispatch, getState) => {
		const filter = getState().filter;
		dispatch({type : UPDATE_FILTER, filter : {...filter, interval}});
	}
}
