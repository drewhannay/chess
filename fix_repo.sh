#!/bin/sh

git filter-branch -f --env-filter '

am="$GIT_AUTHOR_EMAIL"
cm="$GIT_COMMITTER_EMAIL"

if [ "$GIT_AUTHOR_NAME" = "yelemis.soung" ]
then
    cm="yelemissoung@gmail.com"
    am="yelemissoung@gmail.com"
fi

export GIT_AUTHOR_EMAIL="$am"
export GIT_COMMITTER_EMAIL="$cm"

'
