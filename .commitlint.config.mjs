export default {
    extends: ['@commitlint/config-conventional'],
    rules: {
        'type-enum' : [
            2,
            'always',
            [
                'feat',
                'fix',
                'perf',
                'docs',
                'chore',
                'ci',
                'build'
            ]
        ],
        'scope-empty': [2, 'never'],
        'subject-case': [2, 'never', ['start-case', 'pascal-case', 'upper-case']],
        'header-max-length': [2, 'always', 100]
    }
};