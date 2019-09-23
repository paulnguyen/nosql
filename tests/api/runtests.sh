
# Cleanup Previous Run
rm -f output/*.html

# Run Ping Tests (Post Startup)
rm -f output/newman-run*.html
newman run tests/00-nosql-ping-checks.json -r cli,html --reporter-html-export output
mv output/newman-run-*.html output/00-nosql-ping-checks.html

# Run Basic Replication Test
rm -f output/newman-run*.html
newman run tests/1.1-nosql-one-to-many-create.json -r cli,html --reporter-html-export output
mv output/newman-run-*.html output/1.1-nosql-one-to-many-create.html
sleep 10
newman run tests/1.2-nosql-one-to-many-checks.json -r cli,html --reporter-html-export output
mv output/newman-run-*.html output/1.2-nosql-one-to-many-checks.html




