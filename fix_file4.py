with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'r') as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    if i == 157:
        new_lines.append('                            }\n')
    elif i == 158:
        new_lines.append('                        }\n')
    else:
        new_lines.append(line)

with open('app/src/main/java/defensivethinking/co/za/a702podcasts/MainActivity.java', 'w') as f:
    f.writelines(new_lines)
