import requests
import sys

# Command line arguments in order: (email, api key, path to write output file)
if (len(sys.argv) != 3):
    sys.exit(1)

email = sys.argv[1]
api_key = sys.argv[2]
url = "http://apilayer.net/api/check?access_key=" + api_key + "&email=" + email + "&smtp=1&format=1"

# Check for SMTP
response = requests.get(url)
data = response.json()
smtp_check = data['smtp_check']

# Write boolean output to file
f = open("ValidEmailOutput.txt", "w")
f.write(str(smtp_check))
f.close()


