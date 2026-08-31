import re

with open('app/src/main/java/com/example/data/sample/SampleData.kt', 'r') as f:
    content = f.read()

# Replace Names
content = content.replace('Marcus Vance', 'Rajesh Negi')
content = content.replace('Alex Johnson', 'Amit Kumar')
content = content.replace('Sarah Jenkins', 'Priya Singh')
content = content.replace('Michael Chang', 'Rahul Sharma')
content = content.replace('Emma Watson', 'Neha Desai')

# Replace Universities
content = content.replace('New York University (NYU)', 'HNB Garhwal University')
content = content.replace('NYU', 'HNBGU')
content = content.replace('Columbia University North', 'NIT Uttarakhand')
content = content.replace('Columbia University', 'NIT Uttarakhand')
content = content.replace('Pratt Institute', 'Govt Medical College Srinagar')

# Replace Locations/Neighborhoods
content = content.replace('Greenwich Village', 'Srikot')
content = content.replace('Morningside Heights', 'Bhaktiyana')
content = content.replace('Brooklyn Heights', 'Gola Bazar')
content = content.replace('New York', 'Srinagar')
content = content.replace('NY 10012', 'Uttarakhand 246174')
content = content.replace('NY 10027', 'Uttarakhand 246174')
content = content.replace('NY 11201', 'Uttarakhand 246174')
content = content.replace('Washington Square', 'Alaknanda River Front')
content = content.replace('142 Mercer St', 'Badrinath Highway')
content = content.replace('Amsterdam Ave', 'Rishikesh Road')
content = content.replace('Clinton St', 'Pauri Road')
content = content.replace('Manhattan', 'Srinagar')
content = content.replace('Brooklyn', 'Srinagar')

# Price adjustments (Optional, but let's make it look like INR)
content = re.sub(r'basePrice = \d+', 'basePrice = 4500', content)
content = re.sub(r'utilitiesPrice = \d+', 'utilitiesPrice = 500', content)
content = re.sub(r'depositPrice = \d+', 'depositPrice = 2000', content)
content = re.sub(r'monthlyPrice = \d+', 'monthlyPrice = 4500', content)
content = re.sub(r'amount = [\d\.]+', 'amount = 4500.0', content)

with open('app/src/main/java/com/example/data/sample/SampleData.kt', 'w') as f:
    f.write(content)
