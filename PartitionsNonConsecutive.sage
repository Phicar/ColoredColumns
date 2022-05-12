def tal2(n):
	s=0
	nn = n//2
	mm = ceil(n/2.0)
	for p in SetPartitions(nn):
		for q in SetPartitions(mm):
			pq = []
			for x in p:
				ss = []
				for a in x:
					ss.append(2*a)
				pq.append(ss)
			for y in q:
				ss = []
				for a in y:
					ss.append(2*a+1)
				pq.append(ss)
			spq = SetPartition(pq)
			if spq.is_noncrossing():
				s+=1
	return s
			
def tal(n):
	s =0
	for p in SetPartitions(n):
		if not p.is_noncrossing():
			continue
		paila = 0
		for x in p:
			if paila ==1:
				break
			l = -1
			for a in x:
				if l==-1:
					l = a%2
				if paila==1:
					break
				#print(l,a)
				if l!=-1 and a%2!=l:
					paila = 1
					break
		if paila==0:
			print(p,paila)
			s+=1
	return s
for n in srange(1,5):
	print(n,tal(n)) #bell_number(ceil(n/2))*bell_number(floor(n/2)))
