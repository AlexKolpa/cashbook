import gulp from 'gulp';
import less from 'gulp-less';
import browserify from 'browserify';
import source from 'vinyl-source-stream';
import uglify from 'gulp-uglify';
import streamify from 'gulp-streamify';
import rename from 'gulp-rename';

const src = 'src/main/web';
const out = 'target/web';

gulp.task('html', () => gulp.src(src + '/**/*.html').pipe(gulp.dest(out)));
gulp.task('less', () => gulp.src(src + '/**/*.less').pipe(less()).pipe(gulp.dest(out)));
gulp.task('js', () => {
	browserify(src + '/index.js')
		.transform('babelify', {presets: ['es2015']})
		.bundle()
		.pipe(source('index.js'))
		.pipe(streamify(uglify()))
		.pipe(rename('bundle.js'))
		.pipe(gulp.dest(out))
});

gulp.task('build', ['html','less', 'js']);
