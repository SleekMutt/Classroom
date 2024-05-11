import { Pagination } from "react-bootstrap";

const PaginationComponent = ({setCurrentPage, totalPages, currentPage}) => {
    return (
<Pagination style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center'
        }}>
          <Pagination.First onClick={() => setCurrentPage(1)} />
          <Pagination.Prev
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 1}
          />
          {
          [...Array(((currentPage - 2 < 1 ? 5 : currentPage + 2) > totalPages? totalPages : (currentPage - 2 < 1 ? 5 : currentPage + 2)) - (currentPage - 2 < 1 ? 1 : currentPage - 2) + 1).keys()].map(x => x + (currentPage - 2 < 1 ? 1 : currentPage - 2)).map((elem, index) => (
            (<Pagination.Item
              key={elem}
              active={elem === currentPage}
              onClick={() => setCurrentPage(elem)}
            >
            {elem}
            </Pagination.Item>)
          ))}
          <Pagination.Next
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          />
          <Pagination.Last onClick={() => setCurrentPage(totalPages)} />
        </Pagination>
    )
}
export default PaginationComponent;




