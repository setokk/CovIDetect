import requests
import sys

# Command line arguments in order: (email, api key, path to write output file)
if (len(sys.argv) != 4):
    sys.exit(1)

email = sys.argv[1]
api_key = sys.argv[2]
url = "http://apilayer.net/api/check?access_key=" + api_key + "&email=" + email + "&smtp=1&format=1"

# Check for MX Found only 
response = requests.get(url)
data = response.json()
data = data['mx_found']

# Write boolean output to file
f = open(sys.argv[3] + "ValidEmailOutput.txt", "w")
f.write(str(data))
f.close()


