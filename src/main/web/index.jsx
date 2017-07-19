import 'babel-polyfill';

import React from 'react';
import {render} from 'react-dom';
import {Provider} from 'react-redux'
import configureStore from './configureStore'
import App from './containers/app';

const store = configureStore();

function Root() {
	return (
		<Provider store={store}>
			<App/>
		</Provider>
	)
}

render(<Root/>, document.getElementById('root'));