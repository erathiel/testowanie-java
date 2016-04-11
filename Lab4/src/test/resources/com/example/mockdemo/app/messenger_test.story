Given a messenger
When set argument to ug.inf.edu.pl
Then test connection should return 0

When set argument to ug.edu.eu
Then test connection should return 1

When set arguments to ug.inf.edu.pl and somemessage
Then send message should return 0

When set arguments to ug.inf.eu and somemessage
Then send message should return 1

When set arguments to ug.inf.edu.pl and ab
Then send message should return 2

When set arguments to pl and some message
Then send message should return 2

When set arguments to pl and ab
Then send message should return 2
